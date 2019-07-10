#! /usr/bin/python
# -*- coding: utf-8 -*-

import os
import git
import threading

print 'Inicio...'

projects_urls = []

file_path = '/home/suporte/eclipse-workspace/BaiasOrdenacao/Links/'
tipo = ['links_projeto_ruby/','links_projeto_java/']
ruby_file = 'links_projeto_ruby_github('
java_file = 'links_projeto_java('
extensao = ').txt'


rejeitados = ['https://github.com/rafaelduarteamaral/WebAppPessoa.git']
def read_files():
    # # lendo arquivos ruby:
    # for i in range(0, 6):
    #     nome_arquivo = file_path + tipo[0] + ruby_file + str(i) + extensao
    #     print nome_arquivo
    #     arquivo = open(nome_arquivo)
    #     for line in arquivo:
    #         if 'githttps' in line:
    #             line_replace = line.replace('githttps','git\nhttps')
    #             list_replace = line_replace.split('\n')
    #             for item in list_replace:
    #                 projects_urls.append(item.strip())
    #             linha = item
    #         else:
    #             linha = line
    #             projects_urls.append(linha.strip())
    
    # lendo arquivos java
    for i in range(0, 71):
        nome_arquivo = file_path + tipo[1] + java_file + str(i) + extensao
        print nome_arquivo
        arquivo = open(nome_arquivo)
        for line in arquivo:
            if 'githttps' in line:
                line_replace = line.replace('githttps','git\nhttps')
                list_replace = line_replace.split('\n')
                for item in list_replace:
                    if item.strip() not in projects_urls and item.strip() not in rejeitados:
                        projects_urls.append(item.strip())
                    else:
                        continue
                linha = item
            else:
                linha = line
                if linha.strip() not in projects_urls and linha.strip() not in rejeitados:
                    projects_urls.append(linha.strip())
        arquivo.close()
    return

def carregar_projetos():
    read_files()
    
    print 'Qtd:'
    print len(projects_urls)
    print ''
    for url_projeto in projects_urls:
        print 'Clonando projeto: ' + url_projeto
        index = projects_urls.index(url_projeto)
        diretorio = '/home/suporte/eclipse-workspace/BaiasOrdenacao/source_projects/projet_' + str(index)
        if not os.path.exists(diretorio):
            os.mkdir(diretorio)
            print 'diretorio criado'
        else:
            print 'diretorio já existe'
        
        git.Git(diretorio).clone(url_projeto)
    
    # print dados
    fout = open('/home/suporte/eclipse-workspace/BaiasOrdenacao/projects.txt','w')
    for projeto in projects_urls:
        fout.write(projeto + '\n')
    fout.close()

th1 = threading.Thread(target=carregar_projetos, args=( ))
th1.start()
print '...Fim'