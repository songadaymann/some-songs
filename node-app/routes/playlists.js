import { Router } from 'express';
import db from '../lib/db.js';
import { nanoid } from 'nanoid';

const router = Router();

router.get('/', (req, res) => {
  db.read();
  res.json(db.data.playlists);
});

router.post('/', (req, res) => {
  const { userId, name, songIds } = req.body;
  if (!userId || !name) return res.status(400).send('Invalid');
  db.read();
  const playlist = { id: nanoid(), userId, name, songIds: songIds || [], comments: [] };
  db.data.playlists.push(playlist);
  db.write();
  res.status(201).json(playlist);
});

router.get('/:id', (req, res) => {
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  res.json(pl);
});

router.post('/:id/add', (req, res) => {
  const { userId, songId } = req.body;
  if (!userId || !songId) return res.status(400).send('Invalid');
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  if (pl.userId !== userId && req.body.key !== process.env.ADMIN_KEY) {
    return res.status(401).send('Unauthorized');
  }
  pl.songIds ||= [];
  if (!pl.songIds.includes(songId)) pl.songIds.push(songId);
  db.write();
  res.status(201).json(pl);
});

router.post('/:id/comments', (req, res) => {
  const { userId, text, parentId } = req.body;
  if (!userId || !text) return res.status(400).send('Invalid');
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  const comment = {
    id: nanoid(),
    userId,
    text,
    parentId: parentId || null,
    date: new Date().toISOString()
  };
  pl.comments.push(comment);
  db.write();
  res.status(201).json(comment);
});

router.get('/:id/comments', (req, res) => {
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  res.json(pl.comments);
});

router.delete('/:id/comments/:commentId', (req, res) => {
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  const comment = pl.comments.find(c => c.id === req.params.commentId);
  if (!comment) return res.status(404).send('Not found');
  if (req.body.userId !== comment.userId && req.body.key !== process.env.ADMIN_KEY) {
    return res.status(401).send('Unauthorized');
  }
  pl.comments = pl.comments.filter(c => c.id !== req.params.commentId);
  db.write();
  res.status(204).send();
});

router.delete('/:id', (req, res) => {
  db.read();
  const pl = db.data.playlists.find(p => p.id === req.params.id);
  if (!pl) return res.status(404).send('Not found');
  if (req.body.userId !== pl.userId && req.body.key !== process.env.ADMIN_KEY) {
    return res.status(401).send('Unauthorized');
  }
  db.data.playlists = db.data.playlists.filter(p => p.id !== req.params.id);
  db.write();
  res.status(204).send();
});

export default router;
