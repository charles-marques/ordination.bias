# /usr/bin/python
# -*- coding: utf-8 -*-

import load_servers_salary as sr

lista = sr.list_servidores()

top10 = lista[:10]
top40 = lista[:40]

top10_M = float(len(filter(lambda x: x[-1] == 'M', top10 ))) / len(top10) * 100
top10_F = float(len(filter(lambda x: x[-1] == 'F', top10 ))) / len(top10) * 100
top40_M = float(len(filter(lambda x: x[-1] == 'M', top40 ))) / len(top40) * 100
top40_F = float(len(filter(lambda x: x[-1] == 'F', top40 ))) / len(top40) * 100

print("==========================================================================")
print("Estatística:")
print("Top10 M; Top10 F; Top40 M; Top40 F;")
print(" {} ;    {} ;   {} ;    {} ;".format(str(top10_M), str(top10_F), str(top40_M), str(top40_F)))

print("==========================================================================")
print("Estatística Geral:")
topM = float(len(filter(lambda x: x[-1] == 'M', lista ))) / len(lista) * 100
topF = float(len(filter(lambda x: x[-1] == 'F', lista ))) / len(lista) * 100
topN = float(len(filter(lambda x: x[-1] == '', lista ))) / len(lista) * 100
print("Top M; Top F; Vazio;")
print(" {} ;    {} ;   {} ;".format(str(topM), str(topF), str(topN)))