FROM postgres:18.1-alpine

COPY ./postgresql.conf /etc/postgresql.conf

EXPOSE 5432

ENTRYPOINT ["docker-entrypoint.sh"]

CMD ["postgres", "-c", "config_file=/etc/postgresql.conf"]