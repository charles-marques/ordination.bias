#! /usr/bin/python
# -*- coding: utf-8 -*-

import os

print 'Ini...'

ordenacoes = [
'Collections.sort(',
'Comparator.comparing(',
'public int compare(',
'OrderBy',
'order by',
'Sort.by(',
'.compareTo(',
'.sorted',
'.sort(',
'.sortBy(',
'.sortBy{',
'.sortBy {',
'.sortWith(',
'select '
]

lista_pessoas = ['pessoa','pessoas','funcionario','funcionarios','cliente','clientes','usuario','usuarios','person','people','employee','employees','client','customers','user','users','login']

def verificar_arquivo(arquivo_path):
    arquivo = open(arquivo_path, 'r')
    for line in arquivo:
        caminho = str(arquivo_path).replace('/home/suporte/Workspace/BaiasOrdenacao/resources/sistemas/java_projects/','').split('/')
        if [x for x in ordenacoes if x in line] != [] and [y for y in lista_pessoas if y in line.lower()] != []:
            output_result.write(caminho[0] + '§ ' + caminho[-1])
            # output_result.write(' Ordenação: ' + str(ordem))
            output_result.write('§ ' + str(line).replace('\n','').replace('\r','') + '\n')
    arquivo.close()

def listar(diretorio, num_item = 0):
    links_diretorios = os.listdir(diretorio)
    links_diretorios = sorted(links_diretorios, key=lambda x: x[0])
    for link in links_diretorios:
        if diretorio == fontes:
            num_item = num_item + 1
            print num_item
        if num_item > 1400:
            break
        endereco = diretorio + '/' + link
        if os.path.isfile(endereco) and endereco.endswith('.java'):
            verificar_arquivo(endereco)
        elif os.path.isdir(endereco):
            listar(endereco)
        else:
            continue

output_result = open('resources/resultados/links_com_ordenacoes_v5.txt','w')
# listar('/home/suporte/Documentos/source_projects_bkp')
fontes = '/home/suporte/Workspace/BaiasOrdenacao/resources/sistemas/java_projects'
listar(fontes)
output_result.close()

print '...Fim'