#! /usr/bin/python
# -*- coding: utf-8 -*-

import psycopg2 as pg
import load_amostra_nomes as ln

def database_version(connection, cursor):
    print ('--------------------------------------------------------------------------------------------')
    # Print PostgreSQL Connection properties
    print ( connection.get_dsn_parameters(),"\n")
    print ('--------------------------------------------------------------------------------------------')
    # Print PostgreSQL version
    cursor.execute("SELECT version();")
    record = cursor.fetchone()
    print("You are connected to - ", record,"\n")

def inserir_nomes(database_name, table_name = None, column_name = None, array_names = None):
    print 'Database: ' + str(database_name)
    print 'Table: ' + str(table_name)
    print '' + str(column_name)
    try:
        connection = pg.connect(
            user = "postgres", 
            password = "jkQm2cmp*", 
            host = "127.0.0.1", 
            port = "5432", 
            database = database_name)

        cursor = connection.cursor()
        # script insercao
        for name in array_names:
            query = "INSERT INTO " + table_name + "(" + column_name + ") values('" + name + "');"
            print query
            cursor.execute(query)
            connection.commit()
        cursor.fetchall()
        # record = cursor.fetchone()

    except (Exception, pg.Error) as error :
        print ("Error while inserting data in PostgreSQL", error)
    finally:
        if(connection):
            cursor.close()
            connection.close()
            print("PostgreSQL connection is closed")

databases = [
    # ['projet_218','autor', 'nome'],
    # ['projet_218','pessoa', 'nome'],
    ['projet_252','TB_PESSOA', 'nome'],
    # ['projet_275','', ''],
    # ['projet_280','', ''],
    ['projet_291','autor', 'nome'],
    ['projet_291','pessoa', 'nome'],
    # ['projet_509','', '']
]

for i in databases:
    inserir_nomes(i[0], i[1], i[2], ln.amostra)

print 'Fim'