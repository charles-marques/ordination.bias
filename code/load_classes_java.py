#! /usr/bin/python
# -*- coding: utf-8 -*-

# print 'Inicio'

def datasource():
    datasource = []
    arquivo = open('resources/classes_java.txt','r')
    for line in arquivo:
        datasource.append(str(line).replace('\n',''))
    arquivo.close()
    return datasource

# dados = datasource()
# print len(dados)

# for linha in dados[:2]:
#     print '"'+linha+'"'
# print 'Fim'