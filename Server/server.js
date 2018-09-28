import express from 'express';
import bodyParser from 'body-parser';
import {graphqlExpress, graphiqlExpress} from 'apollo-server-express';

const port = parseInt(process.env.PORT, 10) || 3000;

app.prepare().then(() => {
    const server = express();
  
    server.use('/graphql', bodyParser.json(), graphqlExpress({schema}));
    server.use('/graphiql', graphiqlExpress({endpointURL: '/graphql'}));
  
    server.get('*', (req, res) => handle(req, res));
  
    server.listen(port, err => {
      if (err) throw err;
      console.log(`> Ready on http://localhost:${port}`);
    });
  });