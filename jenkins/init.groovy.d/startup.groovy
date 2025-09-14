import jenkins.model.Jenkins

def seedJob = Jenkins.instance.getItem('seed-job')
if (seedJob != null) {
    println "Triggering seed-job build at startup..."
    seedJob.scheduleBuild2(0)
} else {
    println "seed-job not found at startup."
}
