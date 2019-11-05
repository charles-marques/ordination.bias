#! /usr/bin/python
# -*- coding: utf-8 -*-

import load_classes_java as cj

print 'Ini...'

# keys_list = ['person','people','employee','employees','client','customers','user','users']
# keys_list = ['person','employeer','customer','client','user']
keys_list = ['person','employer','employee','customer','client','user']
statistics = []
projetos = []
arquivos = cj.datasource()
dados = []
arquivo_saida = open('resources/classes_java_filtradas.txt','w')

def add_statistic(projeto, classe):
    if [x for x in statistics if x[0] == projeto and x[1] == classe ] == []:
        statistics.append([projeto, classe])
        print(projeto, classe)
    return
def add_projeto(projeto):
    if [x for x in projetos if x == projeto] == []:
        projetos.append(projeto)
    return

def verificar_arquivo(arquivo_path):
    caminho = str(arquivo_path).replace('/home/suporte/Workspace/BaiasOrdenacao/resources/sistemas/java_projects/','')
    temp = caminho.split('/')
    projeto = temp[0]
    classe = temp[-1]
    if [x for x in keys_list if x.lower() in classe.lower()] != []:
        add_statistic(projeto, classe)
        add_projeto(projeto)
        dados.append(caminho)
        arquivo_saida.write(str(caminho) + '\n')

def filtrar():
    for item in arquivos:
        verificar_arquivo(item)
    print 'Qtd Total:  14436 '
    print 'Qtd Filtro: ' + str(len(projetos))

filtrar()
arquivo_saida.close()
print '...Fim'