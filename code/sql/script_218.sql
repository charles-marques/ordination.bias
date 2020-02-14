create table autor (
   id serial primary key,
   nome text not null,
   sobrenome text
);
create table livro (
   isbn integer primary key,
   titulo text not null,
   ano_edicao integer,
   edicao integer,
   editora text,
   situacao varchar(1),
   preco_livro numeric(10,2),
   data_compra date
);
create table autor_livro(
   id serial primary key,
   autor_id integer references autor (id) not null,
   livro_id integer references livro (isbn) not null
);
create table pessoa (
   id serial primary key,
   nome text not null,
   telefone varchar(20),
   email varchar(100)
);
create table emprestimo (
   id serial primary key,
   pessoa_id integer references pessoa (id) not null,
   data_emprestimo date not null,
   data_devolucao date,
   livro_id integer references livro (isbn) not null
);