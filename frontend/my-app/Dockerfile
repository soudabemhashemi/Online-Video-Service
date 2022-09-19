FROM node:14 AS build
COPY package.json package-lock.json /home/app/
WORKDIR /home/app
RUN npm install jwt-decode
RUN npm install
COPY . .
RUN #npm install react-scripts
RUN npm run build

FROM nginx:latest
COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf
RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /home/app/build /usr/share/nginx/html
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]