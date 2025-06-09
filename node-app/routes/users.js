import { Router } from 'express';
import db from '../lib/db.js';
import { nanoid } from 'nanoid';

const router = Router();

router.get('/leaderboard', (req, res) => {
  db.read();
  const list = db.data.users
    .slice()
    .sort((a, b) => (b.points || 0) - (a.points || 0));
  res.json(list);
});

router.post('/', (req, res) => {
  const { name } = req.body;
  if (!name) return res.status(400).send('Name required');
  db.read();
  const user = {
    id: nanoid(),
    name,
    points: 0,
    listeningStreak: 0,
    ratingStreak: 0,
    favorites: [],
    savedSearches: [],
    wallet: null,
    preferredUsers: [],
    ignoredUsers: []
  };
  db.data.users.push(user);
  db.write();
  res.status(201).json(user);
});

router.get('/:id', (req, res) => {
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  res.json(user);
});

router.post('/:id/favorites', (req, res) => {
  const { songId } = req.body;
  if (!songId) return res.status(400).send('Invalid');
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  user.favorites ||= [];
  if (!user.favorites.includes(songId)) user.favorites.push(songId);
  db.write();
  res.status(201).json(user.favorites);
});

router.get('/:id/favorites', (req, res) => {
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  res.json(user.favorites || []);
});

router.post('/:id/searches', (req, res) => {
  const { name, params } = req.body;
  if (!name || !params) return res.status(400).send('Invalid');
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  user.savedSearches ||= [];
  user.savedSearches.push({ id: nanoid(), name, params });
  db.write();
  res.status(201).json(user.savedSearches);
});

router.get('/:id/searches', (req, res) => {
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  res.json(user.savedSearches || []);
});

router.get('/:id/preferences', (req, res) => {
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  res.json({ preferred: user.preferredUsers || [], ignored: user.ignoredUsers || [] });
});

router.post('/:id/preferences', (req, res) => {
  const { preferred, ignored } = req.body;
  db.read();
  const user = db.data.users.find(u => u.id === req.params.id);
  if (!user) return res.status(404).send('User not found');
  if (Array.isArray(preferred)) {
    user.preferredUsers = preferred;
  }
  if (Array.isArray(ignored)) {
    user.ignoredUsers = ignored;
  }
  db.write();
  res.status(201).json({ preferred: user.preferredUsers, ignored: user.ignoredUsers });
});

export default router;
