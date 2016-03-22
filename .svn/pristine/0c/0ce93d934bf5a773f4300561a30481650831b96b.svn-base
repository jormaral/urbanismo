#!/usr/bin/python
# -*- coding: utf-8

import os
import os.path
import shutil
import sys
import getopt
import re
from subprocess import check_call, call
from libreria import *

bdRootUser = ''
baseDir = ''
baseDirJboss7 = '/urbr'
tenemosDatosBD = False

# Gestionar las variables y parámetros iniciales
currentDir = os.getcwd()
optList, args = getopt.getopt(sys.argv[1:], "t:b:")
config = dict(optList)
if (not '-t' in config): config['-t'] = '1234567890'



print
print "  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
print
print "    MIGRACION DE LA BASE DE DATOS (DE VERSION 0.6.0 A VERSION 2.0.0)  E D I T O R  F I P"
print "    v0.01"
print
print "    Argumentos:"
print "     -t 135                  : Ejecutar los puntos de la instalación que se deseen"
#print "     -b <directorio base>    : En general se establece en el punto 1. Se usa para cuando"
#print "                               se ejecuta algún punto de la instalación "
print
print "    Parámetros de la instalación ..."
print "    . Directorio actual: %s" % currentDir
print
print "    Importante, prerréqusitos de la instalacion :"
print "     * Tener instalado postgres y postgis"
print "     * A lo largo de la instalación se le preguntará repetidamente la contraseña "
print "       del usuario administrador de la base de datos, generalmente postgres"
print
print "  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
print


# - - - - - - - - - - - - - - - - - - - - - -
# Creación de la base de datos
print
print "1) MIGRACION DE LA BASE DE DATOS"
print "=============================================="
if ( config['-t'].find("1")==-1 ): print "... no procesar ..."
else:

	

		# Coger datos de bd 
		if (not tenemosDatosBD): bdMaquina, bdNombre, bdUsuario, bdContrasena = cogerDatosBD(bdUsuario='na', bdContrasena='na')
		tenemosDatosBD = True
		if (bdRootUser==''): bdRootUser = raw_input("Usuario administrador de BD (ej:postgres): ")

		# Importación del diccionario 
		#  . Crear diccionario.tar
		#     - pg_dump -F tar -a -U postgres -h 10.9.20.94 -n diccionario RPM_Test > diccionario.tar
		#     - pg_restore -U postgres -h 10.9.20.94 -n diccionario -d pru2 diccionario.tar 
		print "Migrando la base de datos, intruduzca la contraseña de %s ... (ej:UrBr09)" % bdRootUser
		# os.popen("pg_restore -U %s -h %s -d %s -n diccionario %s/importacionBD/diccionario.tar > /dev/null" % (bdRootUser, bdMaquina, bdNombre, currentDir) )
		os.popen("psql -U %s -h %s -d %s -f  %s/ActualizacionesDeBaseDatos.sql > /dev/null" % (bdRootUser, bdMaquina, bdNombre, currentDir) )
		os.popen("psql -U %s -h %s -d %s -f  %s/esquemavalidacion.sql > /dev/null" % (bdRootUser, bdMaquina, bdNombre, currentDir) )
		os.popen("psql -U %s -h %s -d %s -f  %s/esquemarefundido.sql > /dev/null" % (bdRootUser, bdMaquina, bdNombre, currentDir) )
		os.popen("psql -U %s -h %s -d %s -f  %s/esquemaconversorfipsipu.sql > /dev/null" % (bdRootUser, bdMaquina, bdNombre, currentDir) )
		
    
  
		
	
	

# Todo ha ido ok
print
print "  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
print "    Migracion de BD terminada "
print "  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
print






