#! /usr/bin/python
# -*- coding: utf-8 -*-

import os

print 'Ini...'

arquivo = open('resources/classes_java.txt','w')

def listar(diretorio, num_item = 0):
    links_diretorios = os.listdir(diretorio)
    links_diretorios = sorted(links_diretorios, key=lambda x: x[0])
    for link in links_diretorios:
        # if num_item > 1400:
        #     break
        if diretorio == projects_repository:
            num_item = num_item + 1
            print num_item
        endereco = diretorio + '/' + link
        if os.path.isfile(endereco) and endereco.endswith('.java'):
            arquivo.write(endereco.replace('\n','').replace('\r','') + '\n')
        elif os.path.isdir(endereco):
            listar(endereco)
        else:
            continue


# projects_repository = '/home/suporte/Workspace/BaiasOrdenacao/resources/sistemas/java_projects'
projects_repository = 'resources/sistemas/java_projects'
listar(projects_repository)

arquivo.close()

print '...Fim'