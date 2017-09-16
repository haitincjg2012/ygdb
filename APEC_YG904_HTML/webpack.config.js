const resolve = require('path').resolve
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const url = require('url')
const publicPath = ''

module.exports = (options = {}) => ({
  entry: {
    vendor: './src/vendor',
    index: './src/main.js'
  },
  output: {
    path: resolve(__dirname, 'dist'),
    filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
    chunkFilename: '[id].js?[chunkhash]',
    publicPath: options.dev ? '/assets/' : publicPath
  },
  module: {
    rules: [{
        test: /\.vue$/,
        use: ['vue-loader']
      },
      {
        test: /\.js$/,
        use: ['babel-loader'],
        exclude: /node_modules/
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader', 'postcss-loader']
      },
      {
        test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
        use: [{
          loader: 'url-loader',
          options: {
            limit: 10000
          }
        }]
      }
    ]
  },
  plugins: [
    new webpack.optimize.CommonsChunkPlugin({
      names: ['vendor', 'manifest']
    }),
    new HtmlWebpackPlugin({
      template: 'src/index.html'
    })
  ],
  resolve: {
    alias: {
      '~': resolve(__dirname, 'src')

    }
  },
  devServer: {
    host: '192.168.7.141',
    //host: '127.0.0.1',
    port: 8040,
    proxy: {
      '/api/': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/yg-systemuser-service/': {//登录接口
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-user-service/': {//实名认证记录查询（分页）
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-systemConfig-service/': {//查询所需地区信息
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/images/': {//图片地址转发
        target: 'http://192.168.7.21',
        changeOrigin: true
      },
      '/common/': {//上传图片
        target: 'http://192.168.7.111',
        changeOrigin: true
      },
      '/yg-cms-service/': {//行情
        target: 'http://192.168.7.111',
        changeOrigin: true
      }
    },
    historyApiFallback: {
      index: url.parse(options.dev ? '/assets/' : publicPath).pathname
    }
  },
  devtool: options.dev ? '#eval-source-map' : '#source-map'
})
