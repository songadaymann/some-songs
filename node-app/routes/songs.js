import { Router } from 'express';
import db from '../lib/db.js';
import { nanoid } from 'nanoid';
import dayjs from 'dayjs';

const router = Router();

function parseDuration(str) {
  if (!str) return null;
  const parts = str.split(':').map(Number);
  if (parts.length === 2) return parts[0] * 60 + parts[1];
  if (parts.length === 3) return parts[0] * 3600 + parts[1] * 60 + parts[2];
  return null;
}

function ratingAverage(id) {
  db.read();
  const list = db.data.ratings.filter(r => r.songId === String(id));
  if (!list.length) return null;
  const map = { good: 1, okay: 0, bad: -1 };
  return list.reduce((a, r) => a + map[r.rating], 0) / list.length;
}

router.get('/', (req, res) => {
  const { q, sort, sale, minRating, maxRating, fromDate, toDate } = req.query;
  let songs = req.app.locals.songsCache || [];
  if (q) {
    const term = q.toLowerCase();
    songs = songs.filter(s => (s.name || '').toLowerCase().includes(term));
  }
  Object.entries(req.query).forEach(([k, v]) => {
    if (k.startsWith('attr_')) {
      const trait = k.slice(5).toLowerCase();
      songs = songs.filter(s => (s.attributes || []).some(a => a.trait_type.toLowerCase() === trait && String(a.value).toLowerCase() === v.toLowerCase()));
    }
  });
  if (sale === 'true') {
    songs = songs.filter(s => s.forSale);
  }
  songs = songs.map(s => ({ ...s, rating_avg: ratingAverage(s.token_id) }));
  if (minRating) {
    songs = songs.filter(s => (s.rating_avg || 0) >= Number(minRating));
  }
  if (maxRating) {
    songs = songs.filter(s => (s.rating_avg || 0) <= Number(maxRating));
  }
  if (fromDate) {
    songs = songs.filter(s => dayjs(s.release_date).isAfter(dayjs(fromDate).subtract(1, 'day')));
  }
  if (toDate) {
    songs = songs.filter(s => dayjs(s.release_date).isBefore(dayjs(toDate).add(1,'day')));
  }
  if (sort === 'rating') {
    songs = songs.slice().sort((a, b) => (b.rating_avg || 0) - (a.rating_avg || 0));
  } else if (sort === 'date') {
    songs = songs.slice().sort((a, b) => new Date(b.release_date) - new Date(a.release_date));
  } else if (sort === 'random') {
    songs = songs.slice().sort(() => Math.random() - 0.5);
  } else if (sort === 'sale') {
    songs = songs.slice().sort((a, b) => (b.forSale === a.forSale) ? 0 : b.forSale ? 1 : -1);
  } else if (sort === 'lastsale') {
    songs = songs.slice().sort((a, b) => new Date(b.lastSale || 0) - new Date(a.lastSale || 0));
  }
  res.json(songs);
});

router.get('/:id', (req, res) => {
  const song = (req.app.locals.songsCache || []).find(s => String(s.token_id) === String(req.params.id));
  if (!song) return res.status(404).send('Song not found');
  res.json(song);
});

router.get('/:id/play', (req, res) => {
  const song = (req.app.locals.songsCache || []).find(s => String(s.token_id) === String(req.params.id));
  if (!song) return res.status(404).send('Song not found');
  const url = song.audio_url || song.animation_url;
  if (!url) return res.status(404).send('No audio');
  const cid = url.replace('ipfs://', '');
  res.redirect(`https://ipfs.io/ipfs/${cid}`);
});

router.post('/:id/rate', (req, res) => {
  const { userId, rating } = req.body;
  if (!userId || !['good', 'okay', 'bad'].includes(rating)) return res.status(400).send('Invalid');

  db.read();
  const existing = db.data.ratings.find(r => r.userId === userId && r.songId === req.params.id);
  if (existing) {
    existing.rating = rating;
    existing.date = new Date().toISOString();
  } else {
    db.data.ratings.push({ id: nanoid(), userId, songId: req.params.id, rating, date: new Date().toISOString() });
  }
  addPoints(userId, 'rate');
  db.write();
  res.status(201).send('Rated');
});

router.delete('/:id/rate', (req, res) => {
  const { userId, key } = req.body;
  if (!userId) return res.status(400).send('Invalid');
  db.read();
  if (key !== process.env.ADMIN_KEY && !db.data.users.find(u => u.id === userId)) {
    return res.status(401).send('Unauthorized');
  }
  db.data.ratings = db.data.ratings.filter(r => !(r.userId === userId && r.songId === req.params.id));
  db.write();
  res.status(204).send();
});

router.post('/:id/comments', (req, res) => {
  const { userId, text, parentId } = req.body;
  if (!userId || !text) return res.status(400).send('Invalid');
  db.read();
  const comment = { id: nanoid(), songId: req.params.id, userId, text, parentId: parentId || null, date: new Date().toISOString() };
  db.data.comments.push(comment);
  db.write();
  res.status(201).json(comment);
});

router.get('/:id/comments', (req, res) => {
  db.read();
  const comments = db.data.comments.filter(c => c.songId === req.params.id);
  res.json(comments);
});

router.delete('/:id/comments/:commentId', (req, res) => {
  db.read();
  const comment = db.data.comments.find(c => c.id === req.params.commentId);
  if (!comment) return res.status(404).send('Not found');
  if (req.body.userId !== comment.userId && req.body.key !== process.env.ADMIN_KEY) {
    return res.status(401).send('Unauthorized');
  }
  db.data.comments = db.data.comments.filter(c => c.id !== req.params.commentId);
  db.write();
  res.status(204).send();
});

function addPoints(userId, type) {
  const user = db.data.users.find(u => u.id === userId);
  if (!user) return;
  const today = dayjs().startOf("day");
  if (type === "listen") {
    const last = user.lastListenedAt ? dayjs(user.lastListenedAt).startOf("day") : null;
    if (!last || today.diff(last, "day") >= 1) {
      if (last && today.diff(last, "day") === 1) {
        user.listeningStreak = (user.listeningStreak || 0) + 1;
      } else {
        user.listeningStreak = 1;
      }
      const pts = Math.min(100, user.listeningStreak * 10);
      user.points = (user.points || 0) + pts;
    }
    user.points = (user.points || 0) + 10;
    user.lastListenedAt = dayjs().toISOString();
  } else if (type === "rate") {
    const last = user.lastRatedAt ? dayjs(user.lastRatedAt).startOf("day") : null;
    if (!last || today.diff(last, "day") >= 1) {
      if (last && today.diff(last, "day") === 1) {
        user.ratingStreak = (user.ratingStreak || 0) + 1;
      } else {
        user.ratingStreak = 1;
      }
      const pts = Math.min(100, user.ratingStreak * 10);
      user.points = (user.points || 0) + pts;
    }
    user.points = (user.points || 0) + 10;
    user.lastRatedAt = dayjs().toISOString();
  }
}

router.post('/:id/listen', (req, res) => {
  const { userId, elapsed } = req.body;
  if (!userId) return res.status(400).send('Invalid');
  db.read();
  db.data.listens.push({ id: nanoid(), userId, songId: req.params.id, date: new Date().toISOString() });
  addPoints(userId, 'listen');
  const user = db.data.users.find(u => u.id === userId);
  if (user) {
    user.listenedSongs ||= [];
    if (!user.listenedSongs.includes(req.params.id)) {
      user.listenedSongs.push(req.params.id);
      if ((req.app.locals.songsCache || []).length && user.listenedSongs.length >= (req.app.locals.songsCache || []).length) {
        user.points += 10000;
      }
    }
    if (elapsed) {
      const song = (req.app.locals.songsCache || []).find(s => String(s.token_id) === String(req.params.id));
      const lenAttr = song && (song.attributes || []).find(a => a.trait_type.toLowerCase() === 'length');
      const sec = parseDuration(lenAttr?.value);
      if (sec && Number(elapsed) >= sec * 0.9) {
        user.points = (user.points || 0) + 10;
      }
    }
  }
  db.write();
  res.status(201).send('Listened');
});

router.post('/:id/broken', (req, res) => {
  const { userId, note } = req.body;
  if (!userId) return res.status(400).send('Invalid');
  db.read();
  db.data.brokenReports.push({
    id: nanoid(),
    userId,
    songId: req.params.id,
    note: note || '',
    date: new Date().toISOString(),
    resolved: false
  });
  db.write();
  res.status(201).send('Reported');
});

export default router;
