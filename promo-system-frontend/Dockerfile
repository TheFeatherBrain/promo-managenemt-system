FROM nginx:alpine

WORKDIR /usr/share/nginx/html

COPY ./dist/promo-frontend/ .

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
