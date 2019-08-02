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

extensoes = ['jquery.js','bootstrap.js','angular.js','storage.ide','.index','jquery-3.4.1.js','event.js',
'jquery.slim.js','trigger.js','jquery-3.2.0.js','bootstrap.js.map','bootstrap.bundle.min.js.map',
'bootstrap.bundle.js.map','bootstrap.min.js.map','jquery-1.7.1.js','.exe','jquery-1.7.1.js','.class',
'.ide','jquery-3.3.1.js','jquery-1.11.1.js','.dex','.ide-wal','.jar','.dll','.mim.js','.bundle.js',
'jquery-2.1.3.min.js','bootstrap.min.js','jquery-1.11.1.min.js','materialize.min.js',
'Chart.bundle.min.js','Chart.min.js','jquery.min.js','jquery.slim.min.js','jquery-1.9.0.min.js',
'jquery-1.7.1.min.js','jquery-ui-1.8.20.min.js','jquery.3.2.1.min.js','bootstrap.bundle.min.js',
'jquery.waypoints.min.js','query-builder.standalone.min.js','query-builder.min.js','angular.min.js',
'workspace.xml']

pacotes = ['src/main/assets/www/','/assets/release/www/','/assets/debug/www/','/vemComigo2/www/',
'platforms/android/cordova/node_modules','/gtas-parent/gtas-webapp/src/main/webapp/dist/js/',
'/Pratica2/cliente/bower_components']

verificados = []

def verificar_arquivo(arquivo_path):
    for pack in pacotes:
        if pack in arquivo_path:
            return
    for item in extensoes:
        if arquivo_path.endswith(item):
            return
    arquivo = open(arquivo_path, 'r')
    for line in arquivo:
        for ordem in ordenacoes:
            if ordem in line and ('Pessoa' in line or 'pessoa' in line or 'name' in line or 'nome' in line or 'Name' in line or 'Nome' in line) :
                output_result.write('Arquivo: ' + arquivo_path + '\n')
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

output_result = open('resources/resultados/links_com_ordenacoes_v1.txt','w')
listar('source_projects')
output_result.close()

print '...Fim'