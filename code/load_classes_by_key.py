#! /usr/bin/python
# -*- coding: utf-8 -*-

# print 'Ini...'

def datasource():
    datasource = []
    arquivo = open('resources/classes_java_filtradas.txt','r')
    for line in arquivo:
        datasource.append(line.replace('\n',''))
    return datasource

dados = datasource()
# print len(dados)
# print dados[:2]