const express = require('express');
const app = express();
const PORT = 3000;

app.get('/hello', (req, res) => {
      res.json({ 
        message: "hello, world!" 
    });
});

app.listen(PORT, () => {
    console.log(`Servern is running on http://localhost:${PORT}`);
});
