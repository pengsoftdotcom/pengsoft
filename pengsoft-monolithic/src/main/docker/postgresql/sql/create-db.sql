create user pengsoft with encrypted password '494dc31c-a4ee-40f9-9263-d0541d1825c0';
create database pengsoft owner pengsoft;
\c pengsoft;
create extension postgis;
create extension postgis_topology;
create extension "uuid-ossp";
