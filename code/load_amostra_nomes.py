#! /usr/bin/python
# -*- coding: utf-8 -*-

from random import seed
from random import randint


nomes = []

# Array [] de [nome, genero]
arquivo = open('resources/amostra-nomes.v2.csv','r')

for line in arquivo:
    nome = line.replace('\n','').strip().split(',')
    nomes.append(nome)
arquivo.close()

# NOMES ENCONTRADOS NO CEARÁ
nomes.append(['ABACOS','M'])
nomes.append(['ABDIANO','M'])
nomes.append(['ABENEZIO','M'])
nomes.append(['ABENILCA','F'])

def load_amostras():
    amostra = []
    seed()
    for i in range(1000):
        value = randint(0, 100787)
        prenome = nomes[value]
        amostra.append( prenome )
    return amostra

# print 'Relatório:'
# print str(len(amostra)) + ' amostras'
# print 'Fim'