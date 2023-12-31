# behrica/polyglot-app

A `deps-new` template to create a polyglot app in Clojure.
It provides a [devcontainer.json](https://containers.dev) configuration which can setup a devcontainer with Clojure,
and optionally with python + R.

It is IDE agnostic, but has some features which ease the devcontainer use from Emacs:

- provides a nrepl start config
- it fixes nrepl port to 12345
- configures port forwarding of port 12345
- it installs as well `clojure-lsp` inside the devcontainer. A working `clojure-lsp` in Emacs when connecting to the devcontainer
  requires a certain Emacs clojure-lsp configuration, see below
- it assumes that we run Emacs localy, while the dev container runs in a Docker anywhere.
- integrates with [enrich-classpath](https://github.com/clojure-emacs/enrich-classpath)
 

This results in being able to do a `cider-connect` to `localhost:12345` from Emacs when setting up the port forward accordingly.
(which can be further automatised by usage of [devpod](https://devpod.sh/)  

It works as well in VSCode and Codespaces unmodified.
VSCode does not need any of the above, as by design "part of VSCode" get executed inside the Docker container (= VSCode server)

## Usage

This is a template project for use with [deps-new](https://github.com/seancorfield/deps-new).
It will produce a new library project when run (assuming you have installed `deps-new` as your `new` "tool")

### 0.1 release
```bash
clojure -Sdeps '{:deps {net.clojars.behrica/polyglot-app {:git/url "https://github.com/behrica/clj-polyglot-app" 
:git/sha "4f0d0bc" :git/tag "v0.1" }}}' -Tnew create :template behrica/polyglot-app :name myusername/mycoollib
```

### 0.2alpha release
There is an alpha version, which integrates [enrich-classpath](https://github.com/clojure-emacs/enrich-classpath). 
It does not use `launchpad` anymore, but a "enriched" nrepl can get started via `enriched-clojure -M:nrepl` 
in the devcontainer when using the 0.2alpha template

```bash
clojure -Sdeps '{:deps {net.clojars.behrica/polyglot-app {:git/url "https://github.com/behrica/clj-polyglot-app" 
:git/sha "0063656" :git/tag "v0.2alpha" }}}' -Tnew create :template behrica/polyglot-app :name myusername/mycoolapp
```



### Parameters for template

You can use those parameters when suing te template:
- :with-python (true/false), default false -> if python feature get added to devcontainer.json
- :with-R (true/false), default false -> if R feature gets added to devcontainer.json

The versions of python and R can be changed in the generated `devcontainer.json`, look here: https://containers.dev/features
for feature "Python" and "R (via apt)"


Run this template project's tests (by default, this just validates your template's `template.edn`
file -- that it is valid EDN and it satisfies the `deps-new` Spec for template files):

    $ clojure -T:build test

## Out-of-the box support for VSCode / Codespaces
Nothing special is needed. Opening the folder of the code with VSCode will propose to "open it in DevContainer", which does the right thing.

## Special support for Emacs/CIDER

The generated configuration fixes the nREPL port to be 12345. This makes remote connections easier, as we can port forward a known port.
The `devcontainer.json` spec contains 12345 as "port to be forwarded", so assuming the "right" (see below) tooling on client side
the forwarding can be automated. 
The generated [launchpad](https://github.com/lambdaisland/launchpad) configuration starts cider-nrepl on port 12345 by default when calling 
`bin/launchpad`

Having done this we can therefore `cider-connect` to `localhost:12345` from inside a local Emacs and it will connect to the nrepl inside teh devcontainer.

### Emacs support for remote `clojure-lsp`
In order to Emacs working with the remote `clojure-lps` server, we need this configuration snippet in Emacs:
See here for [details](https://emacs-lsp.github.io/lsp-mode/page/remote)

```lisp
(lsp-register-client
    (make-lsp-client :new-connection (lsp-tramp-connection "clojure-lsp")
                     :major-modes '(clojure-mode)
                     :remote? t
                     :server-id 'clojure-lsp-remote))
```

## Tooling needed to get it working with Emacs

1. We need a tool which interprets the `devcontainer.json` spec and generates a Docker image out of it and starts this
2. We need to start nREPL server inside the container after it was started. The generate config does not start the nREPL automaticaly.
   This can happen by ssh into the container and execute `bin/launchpad`  
4. We need to make sure, that the nrepl  port is available to our local Emacs

### Usage with Devpod
In my view, usage of [devpod](https://devpod.sh) is currently the simplest way to get 1) and 3) done and works in the same way for devcontainer 
running localy or remotely (like on a ssh remote host , in Kubernetes or other Docker runtimes)
But a more manual approach using [devcontainer-cl](https://github.com/devcontainers/cli) and manual port forwarding with `ssh -L ...` is possible as well.

### Steps when using Emacs and devpod
1) `devpod up .` in code directory or even pointing to GitHub directly which builds and starts container if needed.
    The location where to run the Docker container can be choosen from "local Docker", remote Docker on ssh host, Kubernetes and others 
3) `devpod ssh .` to login into container (setups the port forwarding as well)
4) run `bin/launchpad`  (or `clj -M:nrepl` or `enriched-clojure -M:nrepl` on 0.2alpha)  which starts nREPL sever on port 12345
5) In Emacs open files from inside container via TRAMP `/ssh:xxxxx` or open the local files 
6) In Emacs run `cider-connect` to `localhost:12345`

Step 1) generates as well a new host entry in `.ssh/config` for ease of use for `ssh` . This makes as well all devcontainers (whereever they run) accessible
in a unified way with a simple `ssh host-name`


devpod has as well a graphical user interface for doing 1) and 2)





## License

Copyright © 2023 Carsten Behring

_EPLv1.0 is just the default for projects generated by `deps-new`: you are not_
_required to open source this project, nor are you required to use EPLv1.0!_
_Feel free to remove or change the `LICENSE` file and remove or update this_
_section of the `README.md` file!_

Distributed under the Eclipse Public License version 1.0.
