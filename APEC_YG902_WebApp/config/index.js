// see http://vuejs-templates.github.io/webpack for documentation.
var path = require('path')

module.exports = {
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: './',
    productionSourceMap: true,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    // port: 8065,
    port: 8065,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      '/_node_org': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-user-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/_node_user_org': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-voucher-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-systemConfig-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-message-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/_node_user': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-cms-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-goods-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-product-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-voucher-service': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/common': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/_node_product': {
        target: 'http://192.168.7.111',
          changeOrigin: true
      },
      '/images':{
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/_node_image': {
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/product':{
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/_node_article':{
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
    '/yg-society-service':{
        target: 'http://192.168.7.111',
        changeOrigin: true
      }
    },
    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
}
