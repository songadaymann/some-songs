import { LowSync } from 'lowdb';
import { JSONFileSync } from 'lowdb/node';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const file = path.join(__dirname, '..', 'db.json');
const defaults = {
  users: [],
  playlists: [],
  ratings: [],
  comments: [],
  boardPosts: [],
  listens: [],
  brokenReports: []
};
if (!fs.existsSync(file)) {
  fs.writeFileSync(file, JSON.stringify(defaults, null, 2));
}

const adapter = new JSONFileSync(file);
const db = new LowSync(adapter, defaults);
db.read();

db.data ||= defaults;
db.write();

export default db;
