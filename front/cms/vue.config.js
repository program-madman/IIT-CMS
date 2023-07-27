const path = require("path");
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack');
module.exports = {
  
  transpileDependencies: [
    'vuetify'
  ],

  // devServer: {
  //   host: 'localhost',
  //   port: 8080,
  //   proxy: {
  //     '/api': {
  //       //target: "http://119.3.192.37:8093/",
  //       target: "http://10.2.6.220:8080/",
  //       changeOrigin: true, // 开启代理
  //       ws: true, // 是否启用 websockets
  //       pathRewrite: {
  //         '/api': ''
  //       }
  //     }
  //   }
  // },

  productionSourceMap: false,
  outputDir: path.resolve(__dirname, "./mymessage"),
  publicPath: process.env.NODE_ENV === 'production'
    ? '/mymessage/'
    : '/',
  // publicPath: '/mymessage/',
  
 
  configureWebpack: config => {
    if (process.env.NODE_ENV === 'production') { // 为生产环境修改配置... 
      config["externals"] = {
        tinymce: "tinymce",
      };
      config.plugins.push(
        new UglifyJsPlugin({
          uglifyOptions: {
            // 删除注释
            output: {
              comments: false
            },
            chunkFilter: (chunk) => {
              // `vendor` 块不压缩
              if (chunk.name === 'vendor') {
                return false;
              }

              return true;
            },
            // 删除console debugger 删除警告
            compress: {
              drop_console: true, // console
              drop_debugger: true,
              pure_funcs: ['console.log']// 移除console
            },
            warnings: false,	//	这样写就不报错  
            sourceMap: false,
            parallel: false,
          }
        }));
      config.plugins.push(new webpack.optimize.LimitChunkCountPlugin({
        maxChunks: 10,
      }));
      

      config.plugins.push(new webpack.optimize.MinChunkSizePlugin({
        minChunkSize: 10000, // Minimum number of characters
      }));

      config.plugins.forEach((it)=>{
        if(it instanceof HtmlWebpackPlugin){
          it.options.minify.removeScriptTypeAttributes = false;
        }
      })

      config["performance"] = {
        "maxEntrypointSize": 10000000,
        "maxAssetSize": 30000000
      }
    }
  },
  pluginOptions: {
    i18n: {
      locale: 'zh',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: true
    }
  }
}
