# Node Refactor Prototype

This directory contains a prototype Node.js implementation for the legacy Java based song rating application. It uses Express and Algolia to fetch song data for Jonathan Mann's songs.

## Setup

1. Install dependencies:

```bash
npm install
```

2. Copy `.env` and fill in the required values:

```
ALGOLIA_APP_ID=YourAlgoliaAppId
ALGOLIA_SEARCH_KEY=8I4QUDIYPJ
ALGOLIA_INDEX_NAME=songs
ALCHEMY_API_KEY=YourAlchemyKey
OPENSEA_API_KEY=YourOpenseaKey
CONTRACT_ADDRESS=0x19b703f65aa7e1e775bd06c2aa0d0d08c80f1c45
ADMIN_KEY=SecretModerationKey
```

3. Start the server:

```bash
node index.js
```

Then open `http://localhost:3000` to view the web interface where fans can
browse songs, rate them, build playlists, chat on the message board and view a
**Collection** page showing the NFTs they own. Users may register with a name
or connect a wallet to log in.

The server exposes the following endpoints:

- `GET /songs` – list songs
  - query parameters: `q` (search text), `sort` (`rating`, `date`, `random`, `sale`, `lastsale`),
    `sale=true`, `attr_*=value`, `minRating`, `maxRating`, `fromDate`, `toDate`
- `GET /songs/:id` – get one song
- `GET /songs/:id/play` – redirect to the audio URL
- `POST /songs/:id/rate` – rate a song (`{userId, rating}`)
- `DELETE /songs/:id/rate` – remove a user's rating (`{userId, key?}`)
- `POST /songs/:id/listen` – record listen (`{userId, elapsed?}`) – full listens earn extra points
- `POST /songs/:id/broken` – report broken link (`{userId, note?}`)
- `GET /songs/:id/comments` – list comments
- `POST /songs/:id/comments` – add comment (`{userId, text, parentId?}`)
- `DELETE /songs/:id/comments/:commentId` – delete comment (`{userId?, key?}`)
- `POST /users` – register user
- `GET /users/:id` – user profile
- `POST /users/:id/favorites` – add favorite song (`{songId}`)
- `GET /users/:id/favorites` – list favorites
- `POST /users/:id/searches` – save search (`{name, params}`)
- `GET /users/:id/searches` – list saved searches
- `GET /users/:id/preferences` – get preferred/ignored users
- `POST /users/:id/preferences` – update preferred/ignored users (`{preferred?, ignored?}`)
- `GET /users/leaderboard` – list users ordered by points
- `GET /playlists` – list playlists
- `POST /playlists` – create playlist (`{userId, name, songIds}`)
- `GET /playlists/:id` – get playlist
- `POST /playlists/:id/add` – add song to playlist (`{userId, songId}`)
- `POST /playlists/:id/comments` – comment on playlist (`{userId, text, parentId?}`)
- `GET /playlists/:id/comments` – list playlist comments
- `DELETE /playlists/:id/comments/:commentId` – delete playlist comment (`{userId?, key?}`)
- `DELETE /playlists/:id` – delete playlist (`{userId?, key?}`)
- `GET /board` – list message board posts
- `POST /board` – create board post (`{userId, text, parentId?}`)
- `DELETE /board/:id` – remove board post (`?key=ADMIN_KEY`)
- `GET /admin/broken` – list broken link reports (`?key=ADMIN_KEY`)
- `POST /admin/broken/:id/resolve` – mark broken report resolved (`?key=ADMIN_KEY`)
- `GET /admin/users` – list all users (`?key=ADMIN_KEY`)
- `DELETE /admin/users/:id` – delete a user (`?key=ADMIN_KEY`)
- `PATCH /admin/users/:id` – edit user data (`?key=ADMIN_KEY`)
- `POST /wallet/login` – login via wallet signature (`{address, signature}`)
- `GET /wallet/collection/:address` – NFT token IDs owned by the wallet

A daily cron job refreshes the song list from Algolia.

## Points
- Listening streak rewards 10 points on day one, increasing by 10 each consecutive day up to 100.
- Rating streaks work the same way.
- Each listen grants 10 points.
- Listening to a song all the way adds 10 more points.
- Each rating also grants 10 points.
- Listening to every song once awards 10,000 points.
