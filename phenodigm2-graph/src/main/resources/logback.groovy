appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n"
    }
}

logger("uk.ac.sanger.phenodigm2", INFO)

root(INFO, ["CONSOLE"])