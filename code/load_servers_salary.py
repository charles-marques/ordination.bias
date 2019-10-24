#! /usr/bin/python
# -*- utf-8 -*-

import load_amostra_nomes as ln
nomes = ln.nomes

print('')
def list_servidores():
    arquivo = open('resources/7406_integration_servers_server_salary.csv','r')
    servidores = []
    primeira = True
    for line in arquivo:
        if primeira:
            primeira = False
            continue
        linha = line.replace('\n','').strip().split(',')
        paramentro = linha[0].split(' ')[0]
        resultado = [x for x in nomes if paramentro == x[0]]
        if resultado != None and resultado != []:
            linha.append(resultado[0][1])
        else:
            linha.append(' ')
        # print(str(linha[-1]) + ' # ' + str(linha[0]))
        # print(linha)
        servidores.append(linha)
    arquivo.close()
    servidores = sorted(servidores, key=lambda x: x[0])
    return servidores

print('')