
setwd("/home/kemal/Downloads")
my_data <- read.delim("nHnWnD-RespPyldSizeNECC.txt",header = FALSE,sep = " ", dec = ".")
head(my_data)
colnames(my_data) <- c("Number of Nodes","RespPyldSizeNECC")

plot(my_data)

library(ggplot2)



df <- data.frame(Nodes=as.factor(my_data$`Number of Nodes`), RespPyldSizeNECC=my_data$RespPyldSizeNECC)

p<-ggplot(data=df, aes(x=Nodes, y=RespPyldSizeNECC)) +
  geom_bar(stat = "identity")

p
