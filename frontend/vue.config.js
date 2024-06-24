const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  lintOnSave: false,

  outputDir: "../src/main/resources/static",
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8090',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
})

