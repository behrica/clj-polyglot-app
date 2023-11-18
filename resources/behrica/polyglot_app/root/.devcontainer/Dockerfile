FROM mcr.microsoft.com/devcontainers/java:11-bullseye

RUN bash -c "bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)"
RUN bash -c "bash < <(curl -s https://raw.githubusercontent.com/clojure-lsp/clojure-lsp/master/install)"

RUN wget https://raw.githubusercontent.com/clojure-emacs/enrich-classpath/master/tools.deps/src/cider/enrich_classpath/clojure.sh -O /usr/local/bin/clojure.sh && chmod +x /usr/local/bin/clojure.sh

RUN echo '#!/bin/bash\n/usr/local/bin/clojure.sh clojure "$@"\n' > /usr/local/bin/enriched-clojure.sh
RUN chmod +x /usr/local/bin/enriched-clojure.sh
