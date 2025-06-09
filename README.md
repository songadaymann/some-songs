# Some Songs Refactor

This repository previously contained a Java/Spring based application for managing multiple artists. The goal is to migrate the project to a single-artist Node.js implementation.

The `node-app` directory contains an initial Express server that loads song data from Algolia and exposes basic endpoints. It demonstrates how new functionality can be added in Node.js.

See `node-app/README.md` for setup instructions. Running `node index.js` will
start the API and serve a small web interface from the `public/` folder where
fans can browse songs, rate them, create playlists, connect their wallet and view
their NFT collection.
