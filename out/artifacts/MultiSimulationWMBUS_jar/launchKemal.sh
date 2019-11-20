#!/bin/bash
TELEGRAM_BOT_TOKEN=1044160993:AAFxGHj5EWU94A1jgLD6aw9o8SWW_RoUYQc
function notifyT  {
	curl -X POST      -H 'Content-Type: application/json' -d "{\"chat_id\": \"362441732\", \"text\": \"$1\", \"disable_notification\": true}" https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage
}
n=-70
notifyT "Simulations Fabio Starts"
#java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 33  --noisevariance $n &
#java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 34  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 32  --noisevariance $n -iss 1000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 32  --noisevariance $n -iss 10000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 32  --noisevariance $n -iss 100000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 31  --noisevariance $n -iss 1000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 31  --noisevariance $n -iss 10000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 31  --noisevariance $n -iss 100000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 30  --noisevariance $n -iss 1000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 30  --noisevariance $n -iss 10000 &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non 50 -ls wHnWwD --detailnoiselevel 30  --noisevariance $n -iss 100000 &
sleep 1
wait
notifyT "Simulations Fabio Finish"
