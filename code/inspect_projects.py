#! /usr/bin/python
# -*- coding: utf-8 -*-

import os

print 'Ini...'

ordenacoes = [
'.sort(',
'.compareTo(',
'public int compare',
'.sortBy(',
'.sortBy{',
'.sortBy {',
'.sortWith(',
'.sorted',
'Sort.by(',
'OrderBy',
'order by'
]

excecoes = ['jquery.js']
# excecoes = ['README.MD','TimeZoneTable.java','jquery.js','angular.js', 'bootstrap.js','jquery-1.9.0.min.js','bootstrap.min.js','jquery-3.4.1.js','jquery-1.7.1.js']
# ,'4.4373bba2440f75f9ce72.js','2.e8e780fa4de259257ef8.js','3.6d4da6261806222a20b9.js','worker-xquery.js','main.2e69105bf311bdf6cb64.js'

verificados = ['node_modules']
# verificados = ['projet_18','projet_301','projet_304','projet_311','projet_440','projet_493','projet_252']
# ,'jquery','javascripts','node_modules','angular2','js','jQuery.1.7.1.1','assets','src-min','src-min-noconflict'

output_result = open('resources/resultados/report4.txt','w')

def verificar_arquivo(arquivo_path):
    # if not arquivo_path.endswith('.scala'):
    #     return 
    if arquivo_path.endswith(".jar") or arquivo_path.endswith(".min.js") or arquivo_path.endswith('.bundle.js'):
        return
    for item in excecoes:
        if item in arquivo_path:
            return
    arquivo = open(arquivo_path, 'r')
    for line in arquivo:
        for ordem in ordenacoes:
            if ordem in line and ('Pessoa' in line or 'pessoa' in line or 'name' in line or 'nome' in line or 'Name' in line or 'Nome' in line) :
                output_result.write('Arquivo: ' + arquivo_path)
                # output_result.write('Ordenação: ' + str(ordem))
                # output_result.write('Linha: ' + line)
    arquivo.close()
def listar(diretorio):
    for verificado in verificados:
        if diretorio.endswith(verificado):
            return
    links_diretorios = os.listdir(diretorio)
    for link in links_diretorios:
        endereco = diretorio + '/' + link
        if os.path.isdir(endereco):
            listar(endereco)
        else:
            verificar_arquivo(endereco)


listar('source_projects')
output_result.close()

print '...Fim'