# /usr/bin/python
# -*- coding: utf-8 -*-

import psycopg2 as pg
import load_amostra_nomes as ln

def consulta():
    ##  ADICIONAR CONSULTAR E FILTROS REALIZADOS PELAS APLICAÇÕES  ##
    """ Projet_218 """
    nomePessoa = 'o'
    ## === SQL DA CONSULTA === ##
        # if nomePessoa == "":
        #     QUERY = "SELECT * FROM pessoa ORDER BY nome"
        # else:
        #     QUERY = "SELECT * FROM pessoa WHERE upper(nome) LIKE '%" + nomePessoa.upper() + "%' ORDER BY nome"
    ## === FILTRO EQUIVALENTE === ##
    resultado = sorted(nomes, key=lambda x: x[0])
    resultado = [k for k in resultado if nomePessoa.strip().upper() in k[0].strip().upper()]
    print("Tamanho:")
    print(len(resultado))
    print('resultado')
    for i in resultado[:10]:
        print(str(resultado.index(i) + 1) + ' ' + str(i))
    
    """ Projet_252 """
    ## === CONSULTA SISTEMA === ##
    rbtMasculinoConsulta = None
    rbtNomeOrder = None
    nome = None
    cpf = None
    sexo = 'M' if rbtMasculinoConsulta == True else 'F'
    orderBy = "nome" if rbtNomeOrder == True else "cpf"
    if orderBy  == "nome":
        resultado = sorted(nomes, key=lambda x: x[0])
    else:
        # resultado = sorted(nomes, key=lambda x: x[2])
        print('# cpf indisponível')
    if nome is not None:
        resultado = [k for k in resultado if nome.strip().upper() in k[0].upper()]
    if sexo is not None:
        resultado = [k for k in resultado if nome.strip().upper() in k[1].upper()]
    if cpf is not None:
        # resultado = [k for k in resultado if nome.strip().upper() in k[2].upper()]
        print('# cpf indisponível')
    print("Tamanho:")
    print(len(resultado))
    print('resultado')

    # ================
    """ Projet_275 """
    

    # ================
    """ Projet_280 """
    # ================
    """ Projet_291 """
    # ================
    """ Projet_509 """
    """ Projeto_12 """
    """ Projeto_14 """
    """ Projeto_192 """
    """ Projeto_224 """
    """ Projeto_233 """
    """ Projeto_339 """
    """ Projeto_581 """
    """ Projeto_587 """
    """ Projeto_611 """
    """ Projeto_630 """
    """ Projeto_687 """
    """ Projeto_719 """
    """ Projeto_752 """
    """ Projeto_949 """
    """ Projeto_987 """
    """ Projeto_1065 """
    """ Projeto_1086 """
    """ Projeto_1095 """
    """ Projeto_1100 """
    """ Projeto_1120 """
    """ Projeto_1171 """
    """ Projeto_1208 """
    """ Projeto_1220 """
    """ Projeto_1236 """
    """ Projeto_1260 """
    """ Projeto_1275 """
    """ Projeto_1295 """
    """ Projeto_1324 """
    """ Projeto_1328 """
    """ Projeto_1336 """
    """ Projeto_1364 """
    """ Projeto_1369 """
    """ Projeto_1480 """
    """ Projeto_1609 """
    """ Projeto_1613 """
    """ Projeto_1614 """
    """ Projeto_1662 """
    """ Projeto_1748 """
    """ Projeto_1812 """
    """ Projeto_1827 """
    """ Projeto_1847 """
    """ Projeto_1849 """
    """ Projeto_1850 """
    print("...Fim")

nomes = ln.amostra[:1000]
print(len(nomes))
# configuracao()
consulta()