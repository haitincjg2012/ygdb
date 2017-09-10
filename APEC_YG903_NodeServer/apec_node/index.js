var srcPath = (process.env.NODE_ENV || 'development') === 'test-cov' ? './lib-cov' : './src';
var app = exports = module.exports = require(srcPath);
var port = process.env.PORT || 4100
if (require.main === module) {
  app.listen(port, function () {
    console.log('apec daiban node server listened');
  });
}
