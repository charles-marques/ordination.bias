#! /usr/bin/python
# -*- coding: utf-8 -*-

import load_classes_by_key as ck
# import load_classes_java as cj

print 'ini'

dados = ck.datasource()
estatistica_ordenacao = []
estatistica_propriedades = []
estatistica_listas = []
geral = []
classes_ranking = {}
keys_list = ['person','employer','employee','customer','client','user']

ordenacoes = ['Collections.sort(', '.sort(','Comparable<','Comparator<','order by','OrderBy','Sort.by(','.compareTo(','.sorted','.sortBy(']

propriedades = ['name','age','email']

listas = ['List<']

def add_estatistica_ordenacao(projeto_ord):
    if [x for x in estatistica_ordenacao if x == projeto_ord] == []:
        estatistica_ordenacao.append(projeto_ord)
    return
def add_estatistica_propriedades(projeto_prop):
    if [x for x in estatistica_propriedades if x == projeto_prop] == []:
        estatistica_propriedades.append(projeto_prop)
    return
def add_estatistica_listas(projeto_lista):
    if [x for x in estatistica_listas if x == projeto_lista] == []:
        estatistica_listas.append(projeto_lista)
    return
def add_geral(projeto):
    if [x for x in geral if x == projeto] == []:
        geral.append(projeto)
    return
def add_class_ranking(nome_classe):
    if nome_classe in classes_ranking:
        classes_ranking[nome_classe] = classes_ranking[nome_classe] + 1
    else:
        classes_ranking[nome_classe] = 1
    return

for item in dados:
    caminho = item.replace('\n','')
    arquivo = open('resources/sistemas/java_projects/' + caminho, 'r')
    temp = caminho.split('/')
    projeto = temp[0]
    classe = temp[-1]
    add_geral(projeto)
    i = 0
    for line in arquivo:
        if [x for x in keys_list if x.lower() in line.lower() and 'List<'.lower() in line.lower() ] != []:
            add_estatistica_listas(projeto)
            linha = line.split(' ')
            linha = [x for x in linha if 'List<'.lower() in x.lower()]
            lista_ordenada = linha[0].replace('\n','').replace('\t','')
            print lista_ordenada
            add_class_ranking(lista_ordenada)
        # if [x for x in ordenacoes if x.lower() in line.lower() ] != []:
        #     add_estatistica_ordenacao(projeto)
        # if [y for y in propriedades if y.lower() in line.lower() ] != []:
        #     add_estatistica_propriedades(projeto)
        #     add_class_ranking(classe)
    arquivo.close()

ordenado = sorted(classes_ranking.items(), key=lambda x: x[1], reverse=True)

print 'Qtd Projetos    : ' + str(len(geral))
print 'Qtd Ordenações  : ' + str(len(estatistica_ordenacao))
print 'Qtd Propriedades: ' + str(len(estatistica_propriedades))
print 'Qtd Listas Orden: ' + str(len(estatistica_propriedades))
for i in ordenado[:10]:
    print i

print 'Fim'
