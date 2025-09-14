pipelineJob('pingpong-promotion-dev') {
  description('Triggered by Docker Hub webhook: bumps image version in dev overlay and opens PR.')

  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('https://github.com/daoquocquyen/pingpong-cicd.git')
            // credentials('github-cred-id') // optional, if needed
          }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/promotion/dev/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-verify-dev') {
  description('Runs tests for environments changed in pingpong-gitops-config/main using Argo CD.')
  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('https://github.com/daoquocquyen/pingpong-cicd.git')
          }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/verify/dev/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-promote-qa') {
  description('Manual: Create a single PR to promote all services from the tested dev baseline to QA.')
  parameters {
    stringParam('BASELINE_SHA', '', 'GitOps commit SHA that passed dev smoke tests (from baseline.txt or build description).')
  }
  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('https://github.com/daoquocquyen/pingpong-cicd.git')
          }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/promotion/qa/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-verify-qa') {
  description('Manual: Verify QA environment is healthy and passing checks (Argo CD sync + tests).')
  definition {
    cpsScm {
      scm {
        git {
          remote { url('https://github.com/daoquocquyen/pingpong-cicd.git') }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/verify/qa/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-promote-prod') {
  description('Manual: Tag source/images (optional) and promote tested QA baseline to Prod in one PR.')
  parameters {
    stringParam('BASELINE_SHA', '', 'GitOps commit SHA to use as baseline (typically the QA-tested commit).')
  }
  definition {
    cpsScm {
      scm {
        git {
          remote { url('https://github.com/daoquocquyen/pingpong-cicd.git') }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/promotion/prod/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-verify-prod') {
  description('Manual: Verify Prod environment is healthy and passing checks (Argo CD sync + synthetic).')
  definition {
    cpsScm {
      scm {
        git {
          remote { url('https://github.com/daoquocquyen/pingpong-cicd.git') }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/verify/prod/Jenkinsfile')
    }
  }
}

pipelineJob('pingpong-verify-dispatch') {
  description('Dispatches GitOps push events to the appropriate env verify jobs (dev/qa/prod).')
  definition {
    cpsScm {
      scm {
        git {
          remote { url('https://github.com/daoquocquyen/pingpong-cicd.git') }
          branch('main')
        }
      }
      scriptPath('Jenkinsfiles/verify/dispatch/Jenkinsfile')
    }
  }
}
