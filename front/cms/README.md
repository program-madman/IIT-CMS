# cms

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

npm install -g serve
# -s flag means serve it in Single-Page Application mode
# which deals with the routing problem below
serve -s dist

# push to server
scp -r ./wapper backend@10.2.6.220:/app/cms/apache-tomcat-8.5.73/webapps/

# login test user
http://10.2.6.220:8280/login?token=1&gsid=79601076&jb=admin&yhid=1005953523274843919
