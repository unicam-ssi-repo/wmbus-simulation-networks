#!/bin/bash
TELEGRAM_BOT_TOKEN=1044160993:AAFxGHj5EWU94A1jgLD6aw9o8SWW_RoUYQc
function notifyT  {
        curl -X POST      -H 'Content-Type: application/json'      -d "{\"chat_id\": \"362441732\", \"text\": \"$1\", \"disable_notification\": true}" https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage
}
SECONDS=0
noise=(-75.0 -70.0)
for n in "${noise[@]}";
do
# Without route decision
# With and without hamming
sleep 1
java -jar MultiSimulationWMBUS.jar -sis false --multi -non $1 -ls nHnWnD --noisevariance $n &
sleep 1
java -jar MultiSimulationWMBUS.jar -sis false --multi -non $1 -ls wHnWnD --noisevariance $n &
sleep 1
# With route decision
# Without hamming
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls nHnWnD --noisevariance $n &
sleep 1
# With hamming and no detailed noise.
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWnD --noisevariance $n &
sleep 1
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 4  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 8  --noisevariance $n &
wait
duration=$SECONDS
notifyT "50% done with $1 Nodes done with $n noise.  $(($duration / 60)) minutes and $(($duration % 60)) seconds elapsed."
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 16  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 32  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 64  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 128  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 192  --noisevariance $n &
java -jar MultiSimulationWMBUS.jar --multi -non $1 -ls wHnWwD --detailnoiselevel 256  --noisevariance $n &
wait
duration=$SECONDS
notifyT "$1 Nodes done with $n dbm noise done. $(($duration / 60)) minutes and $(($duration % 60)) seconds elapsed."
done



