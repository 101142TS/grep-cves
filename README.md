# hunter

# cmd

将apk文件转换成Jimple格式   /home/exia/platforms文件夹
java -Xmx4g -jar soot-4.2.1-jar-with-dependencies.jar soot.Main -w -allow-phantom-refs -android-jars /home/exia/platforms/ -src-prec apk -f jimple -process-dir com.tencent.mtt.apk -process-multiple-dex


将Jimple转换成Java

java -Xmx8g -jar soot-4.2.1-jar-with-dependencies.jar soot.Main -soot-class-path /home/exia/platforms/android-29/android.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar   -allow-phantom-refs -pp -w  -src-prec jimple -process-path ./sootOutput -d Java


java -Xmx4g -jar soot-4.2.1-jar-with-dependencies.jar soot.Main -soot-class-path /home/exia/platforms/android-29/android.jar  -pp -w  -src-prec jimple -process-path ./sootOutput -d Java

将apk文件转换成Java格式   /home/exia/platforms文件夹
java -Xmx4g -jar soot-4.2.1-jar-with-dependencies.jar soot.Main -w -allow-phantom-refs -android-jars /home/exia/platforms/ -src-prec apk -f t -process-dir com.tencent.mtt.apk -process-multiple-dex -d Java

将apk文件转换成.class格式   /home/exia/platforms文件夹
java -Xmx4g -jar soot-4.2.1-jar-with-dependencies.jar soot.Main -w -allow-phantom-refs -android-jars /home/exia/platforms/ -src-prec apk -f class -process-dir com.tencent.mtt.apk -process-multiple-dex -d Java