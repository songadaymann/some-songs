import { Router } from 'express';
import db from '../lib/db.js';

const router = Router();

router.use((req, res, next) => {
  if (req.query.key !== process.env.ADMIN_KEY) {
    return res.status(401).send('Unauthorized');
  }
  next();
});

router.get('/broken', (req, res) => {
  db.read();
  res.json(db.data.brokenReports);
});

router.post('/broken/:id/resolve', (req, res) => {
  db.read();
  const report = db.data.brokenReports.find(r => r.id === req.params.id);
  if (!report) return res.status(404).send('Not found');
  report.resolved = true;
  db.write();
  res.status(200).send('Resolved');
});

export default router;
