cd /d %~dp0

@echo off
set /p frpath= Please enter the install path of your FineBI?
set _trimmed=%frpath%
:loop
 if not "%_trimmed:~-1%"==" " goto next
 set _trimmed=%_trimmed:~0,-1%
 goto loop
:next
set frpath=%_trimmed%\webapps\WebReport
@echo install path is %frpath%

if exist "%frpath%"  goto hack
@echo No such folder %frpath%
goto fail


:hack
set resourcepath=%frpath%\WEB-INF\resources
if exist "%resourcepath%"  goto copyresource
@echo No such folder %resourcepath%
goto fail

:copyresource
@echo Copying FineBI.lic to %resourcepath%

cp FineBI.lic "%resourcepath%"
rem dir %resourcepath%
@echo Success to copy FineReport.lic to %resourcepath%

set libpath=%frpath%\WEB-INF\lib
rem dir %libpath%

if exist "%libpath%"  goto frcore
@echo No such folder %libpath%
goto fail

:frcore
set frcorefile=%libpath%\fr-core-8.0.jar
@echo fr code file path is %frcorefile%

if exist "%frcorefile%"  goto hackjar
@echo No fr-core-8.0.jar found at %frcorefile%
goto fail

:hackjar
@echo Hacking FineReport

jar uf "%frcorefile%" com\fr\data\VersionInfoTableData.class || (@echo File "%frcorefile%"in used, please exit fineBI and try again & goto fail)
jar uf "%frcorefile%" com\fr\stable\LicUtils.class || (@echo File "%frcorefile%" in used, please exit fineBI and try again & goto fail)
jar uf "%frcorefile%" com\fr\general\FUNC.class || (@echo File "%frcorefile%" in used, please exit fineBI and try again & goto fail)


@echo Cong! you have successfully hacked FineBI!
@echo Please restart the server!
pause > nul
exit

:fail
@echo Fail to hack FineBI
pause > nul
exit