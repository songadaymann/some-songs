import { Router } from 'express';
import db from '../lib/db.js';
import { nanoid } from 'nanoid';

const router = Router();

router.get('/', (req, res) => {
  db.read();
  res.json(db.data.boardPosts);
});

router.post('/', (req, res) => {
  const { userId, text, parentId } = req.body;
  if (!userId || !text) return res.status(400).send('Invalid');
  db.read();
  const post = {
    id: nanoid(),
    userId,
    text,
    parentId: parentId || null,
    date: new Date().toISOString()
  };
  db.data.boardPosts.push(post);
  db.write();
  res.status(201).json(post);
});

router.delete('/:id', (req, res) => {
  if (req.query.key !== process.env.ADMIN_KEY) return res.status(401).send('Unauthorized');
  db.read();
  db.data.boardPosts = db.data.boardPosts.filter(p => p.id !== req.params.id);
  db.write();
  res.status(204).send();
});

export default router;
