const express = require("express");
const bodyParser = require("body-parser");
const app = express();
const swaggerUi = require('swagger-ui-express');
const apiDocumentation = require('./rubistApi.json');
app.use('/rubistApi-docs', swaggerUi.serve, swaggerUi.setup(apiDocumentation));


app.get('/', (req, res) => {
  res.send("API Aktif, hehe");
})

// Routes
const authRoutes = require("./routes/auth");
const updateProfile = require("./routes/profile");
const communityRoutes = require('./routes/communityRoute');

// Middlewares
app.use(express.json());
app.use(express.urlencoded({extended: true}));

// Routes
app.use("/api", authRoutes);
app.use("/api", updateProfile);
app.use('/api/community', communityRoutes);


// PORT

// Starting a server
const server = app.listen(process.env.PORT || 8080, () => {
  const host =server.address().address;
  const port = server.
  address.port;

  console.log ("Runing on port 8080");
});