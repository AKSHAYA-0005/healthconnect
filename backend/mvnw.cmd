@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script for HEALTHCONNECT
@REM ----------------------------------------------------------------------------

@SET "DIRNAME=%~dp0"
@IF "%DIRNAME%" == "" SET "DIRNAME=."

@IF EXIST "%DIRNAME%\.mvn\apache-maven-3.9.6\bin\mvn.cmd" (
  "%DIRNAME%\.mvn\apache-maven-3.9.6\bin\mvn.cmd" %*
  @EXIT /B %ERRORLEVEL%
)

mvn %*
