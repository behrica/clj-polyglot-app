# behrica/polyglot-app

A `deps-new` template to create a polyglot app in Clojure.
It provides a [devcontainer.json](https://containers.dev) configuration which can setup a devcontainer with Clojure, python + R.

It is IDE agnostic, but has some features which ease the devcontainer use from Emacs:

- provides a launchpad config
- it fixes nrepl port to 12345
- configures port forwarding of port 12345
- it installs as well `clojure-lsp` inside the devcontainer. A working `clojure-lsp` in Emacs when connecting to the devcontainer
  requires a certain Emacs clojure-lsp configuration (TODO: details)
- it assumes that we run Emacs localy, while the dev container runs in a Docker anywhere.
 

This results in being able to do a `cider-connect` to `localhost:12345` from Emacs when setting up the port forward accordingly.
(which can be further automatised by usage of [devpod](https://devpod.sh/)  

It works as well in VSCode and Codespaces unmodified.
VSCode does not need any of the above, as by design "part of VSCode" get executed inside the Docker container (= VSCode server)

## Usage

This is a template project for use with [deps-new](https://github.com/seancorfield/deps-new).
It will produce a new library project when run:

    $ clojure -Sdeps '{:deps {net.clojars.behrica/polyglot-app {:git/url "https://github.com/behrica/clj-polyglot-app" :git/sha "891c092e4768511717178f689762bc67eb692cbe"}}}' -Tnew create :template behrica/polyglot-app :name myusername/mycoollib

Assuming you have installed `deps-new` as your `new` "tool" via:

```bash
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
```

> Note: once the template has been published (to a public git repo), the invocation will be the same, except the `:local/root` dependency will be replaced by a git or Maven-like coordinate.

Run this template project's tests (by default, this just validates your template's `template.edn`
file -- that it is valid EDN and it satisfies the `deps-new` Spec for template files):

    $ clojure -T:build test

## Out-of-the box support for VSCode / Codespaces
Nothing special is needed. Opening the folder of the code with VSCode will propose to "open it in DevContainer", which does the right thing.

## Special support for Emacs/CIDER

The configuration generated, fixes the nREPL port to be 12345. This makes remote connections easier, as we can port forward a known port.
The `devcontainer.json` spec contains 12345 as "port to be forwarded", so assuming the "right" (see below) tooling on client side
the forwarding can be automated. 
The generated [launchpad](https://github.com/lambdaisland/launchpad) configuration starts cider-nrepl on port 12345 by default when calling 
`bin/launchpad`

Having done this we can therefore `cider-connect` to `localhost:12345` from inside a local emacs

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

In my view, usage of [devpod](https://devpod.sh) is currently the simplest way to get 1) and 3) done and works in the same way for devcontainer 
running localy or remotely (like on a ssh remote host , in Kubernetes or other runtimes)
But a more manual approach using [devcontainer-cl](https://github.com/devcontainers/cli) and manual port forwarding with `ssh` is possible as well.

### Steps when using Emacs and devpod
1) `devpod up` in code directory or even pointing to GitHub directly which builds and starts container if needed.
    The location where to run the Docker container can be choosen from "local Docker", remote Docker on ssh host, Kubernetes and others 
3) `devpod ssh` to login into container (setups the port forwarding as well) and run `bin/launchpad`  which starts nREPL sever on port 12345
4) In Emacs open files from inside container via TRAMP `/ssh:xxxxx`
5) In Emacs run `cider-connect` to `localhost:12345`

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
