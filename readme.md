# ShipBot Plugin

![CI](https://github.com/oscarg798/ShipBotPlugin/workflows/CI/badge.svg?branch=master)

<div style="text-align:center">
<img src="logo.png" height="200px"/>
</div>


Este plugin sirve para publicar tu `apk` a `Firebase Distribution`, se encarga de generar el mismo y distribuirlo. 

## Prerequisitos

1. Para ejecutar este plugin [Firebase cli](https://firebase.google.com/docs/cli) debe estar configurado en la maquina desde donde estes ejecutando el comando. 

2. Debes logearte con esta herramienta `firebase login` 

3. Debes generar un token para pasarlo a la tarea como veremos más adelante `firebase login:ci`

## Configuración 

En el `build.gradle` principal de tu proyecto agrega lo siguiente al classpath 

```groovy 
   repositories {
    //other repos
     maven {
        url = "https://repo1.maven.org/maven2/"
     }
   }
   dependencies{
        //other dependencies
        classpath "com.oscarg798.shipbotplugin:shipBotPlugin:1.0.0"
   }
```

En tu modulo `app` o el modulo principa de tu aplicación 

```groovy
    plugins { 
        //other plugins like id 'com.android.application'
        id 'shipbot'
    }
```

### parametros

El plugin te permite configurar los siguientes parametros

* **unitTestRequired** un booleano para indicar si se deben correr los unit test antes de generar y publicar tu `app` a `firebase`. Por defecto esta en `true`
* **buildTypes** un arreglo de `Strings` para indicarle al plugin que tipos de build tu proyecto soportara; por defecto solo se soporta `debug`
* **firebaseToken** el `token` de `firebase` con el cual se publicara el apk; esta variable no tiene variable por defecto y es **requerida**
* **flavors** un arreglo de `Strings` para indicarle al plugin los flavors o variantes de tu proyecto desde los cuales se deberara y publicara el build; por defecto este es vació

en tu archivo `build.gradle` de tu modulo `app` o principal puedes configurar los mismo asi:

```groovy
  releasePlugin {
        unitTestRequired = true
        buildTypes = ["debug", "release",...]
        flavors = ["flavor1", "flavor2", ...]
        firebaseToken = "TU_TOKEN"
  }
```

## Ejecución 

una vez configurado el plugin en tu proyecto solo necesitaras ejecutar la siguiente tarea `ship` con gradle. Dicha tarea recibe los siguientes parametros:

* **firebaseProjectId** el id del proyecto de `firebase` en el cual tu apk quedara publicado. Este parametro es **requerido**
* **PdistributionGroup** el nombre del grupo que tendra acceso al `apk` una vez en `firebase`. Este parametro es **opcional**
* **notes** las notas o comentarios con los cuales se publicara el `apk`. Este parametro es **opcional**
* **builtType** el tipo de build que se generara, este parametro es **requerido**
* **flavor** el flavor desde el cual deseas generar el build. Este parameto es **opcional** y solo debes incluirlo si configuraste flavors en el plugin. 


```bash 
 ./gradlew ship -PbuiltType=debug -PfirebaseProjectId=1:2234234234 -PdistributionGroup=testers -Pnotes="do not know why notes not working with spaces" -Pflavor="flavor1"
```