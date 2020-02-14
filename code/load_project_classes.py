#! /usr/bin/python
# -*- coding: utf-8 -*-

import load_classes_by_key as ck
# import load_classes_java as cj

classes = ck.datasource()
stats_project  = []

# keys_list = ['person','employer','employee','customer','client','user']
# ordenacoes = ['Collections.sort(', '.sort(','Comparable<','Comparator<','order by','OrderBy','Sort.by(','.compareTo(','.sorted','.sortBy(']
# propriedades = ['name','age','email']

def add_stats_project(projeto, classe):
    classe = classe.replace('.java','')
    lista_temp = [x for x in stats_project if x[0] == projeto]
    if lista_temp == []:
        stats_project.append([projeto,classe])
    else:
        indice = stats_project.index(lista_temp[0])
        if [x for x in lista_temp[0] if x == classe] == []:
            stats_project[indice].append(classe)

    return

def project_classes_datasource():
    # verificando classes com as palavras chaves
    for classe in classes:
        caminho = classe.replace('\n','')
        arquivo = open('/home/suporte/Documentos/java_projects/' + caminho, 'r')
        temp = caminho.split('/')
        project_name = temp[0]
        classe = temp[-1]
        add_stats_project(project_name, classe)
        arquivo.close()
    return stats_project