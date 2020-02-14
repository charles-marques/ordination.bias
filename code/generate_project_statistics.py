#! /usr/bin/python
# -*- coding: utf-8 -*-

import os
import psycopg2 as pg
import load_project_classes as pc
import load_classes_java as cj

dados = pc.project_classes_datasource()
classes_do_projeto = cj.datasource()
# project_listing = []
projects_repository = '/home/suporte/Documentos/java_projects'

print 'Iniciando configurações de banco...'
conn = None
cur = None

def project_added(name):
    sql = "select * from projects where name = '" + str(name) + "'"
    cur.execute(sql)
    records = cur.fetchall()
    return not (records == None or records == [])

def add_project_name(name):
    # se não existe adicionar
    if not project_added(name):
        cur.execute("INSERT INTO projects (NAME) VALUES ('" + str(name) + "')")
        conn.commit()
        print 'Projeto novo: ' + str(name)
    else:
        print 'Projeto já adicionado: ' + str(name)
    return

def verificar_lista_classes(project_name, lista_classes):
    lista_filtrada = filter(lambda x: project_name in str(x), classes_do_projeto)
    for item in lista_filtrada:
        local_file = item.replace('Workspace/BaiasOrdenacao/resources/sistemas','Documentos')
        arquivo = open(local_file,'r')
        for linha in arquivo:
            # if project_added(project_name):
            #     break
            if [x for x in lista_classes if 'List<' + str(x) + '>' in linha ] != []:
                add_project_name(project_name)
        arquivo.close()
    return

try:
    conn =  pg.connect("dbname=baias user=postgres ")
    cur = conn.cursor()

    print('Listando projetos e verificando...')
    # 3457
    for dado_projeto in dados[4000:]:
        indice = dados.index(dado_projeto) + 1
        print(indice)
        projeto = dado_projeto[0]
        classes = dado_projeto[1:]
        verificar_lista_classes(projeto, classes)
except (Exception, pg.Error) as error:
    print(error)
finally:
    if conn:
        cur.close()
        conn.close()
# print 'tamanho: '
# print len(dados)
print 'Fim'