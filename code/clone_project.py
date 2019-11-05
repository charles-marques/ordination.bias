#! /usr/bin/python
# -*- coding: utf-8 -*-

import os
import git
import threading

print 'Inicio...'

projects_urls = []

nome_arquivo = '/home/suporte/Workspace/BaiasOrdenacao/resources/Links/z_links_geral_v2.txt'

def read_files():
    print nome_arquivo
    arquivo = open(nome_arquivo)
    for line in arquivo:
        linha = line.replace('\n','').strip()
        if linha not in projects_urls:
            projects_urls.append(linha)
    arquivo.close()
    return

def carregar_projetos():
    read_files()
    
    print 'Qtd:'
    print len(projects_urls)
    print ''
    for url_projeto in projects_urls:
        print 'Clonando projeto: ' + url_projeto
        index = projects_urls.index(url_projeto) + 1400
        diretorio = '/home/suporte/Workspace/BaiasOrdenacao/source_projects/projeto_' + str(index)
        if not os.path.exists(diretorio):
            os.mkdir(diretorio)
            print 'diretorio criado'
        else:
            print 'diretorio já existe'
        
        git.Git(diretorio).clone(url_projeto)
    
    # print dados
    fout = open('/home/suporte/eclipse-workspace/BaiasOrdenacao/resources/projects.txt','w')
    for projeto in projects_urls:
        fout.write(projeto + '\n')
    fout.close()

th1 = threading.Thread(target=carregar_projetos, args=( ))
th1.start()
print '...Fim'