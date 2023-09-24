(ns atividade-final-tarefas.core)

(def lista-tarefas (atom []))
;; Os "Átomos" (atom) são um tipo de dados no Clojure que fornecem uma maneira de gerenciar estados compartilhados, síncronos e independentes. Um átomo é como qualquer tipo de referência em qualquer outra linguagem de programação. O principal uso de um átomo é manter as estruturas de dados imutáveis do Clojure.

(defn obter-descricao-tarefa []
  (println "Digite a descricao da tarefa:")
  (read-line))

(defn adicionar-tarefa [descricao]
  ;aqui temos o primeiro exemplo de "Closure" no programa, pois a função adicionar-tarefa faz referência à variável lista-tarefas, que é uma variável definida em uma função externa de onde adicionar-tarefa foi definida.
  (when (not (empty? descricao)) ; Verifica se a descrição não está vazia
    (let [id (if (empty? @lista-tarefas)
               1
               (->> @lista-tarefas (map :id) (apply max) inc)) ;Uso da função "map", em Clojure, é equivalente à List Comprehension, pois para cada valor de uma lista, você aplica uma operação e coloca todos os resultados em uma nova lista.
          nova-tarefa {:id id :descricao descricao}
          ;A função interna nova-tarefa também é uma closure, pois faz referência à variável lista-tarefas, definida em uma função externa.
          ]
      (swap! lista-tarefas conj nova-tarefa))))
;; Uma função de alta ordem é aquela que aceita uma ou mais funções como argumentos e/ou retorna uma função como resultado. A função swap! é uma função de alta ordem em Clojure, pois ela aceita uma função como argumento e a aplica no valor do átomo. Neste caso, a função conj é passada como um argumento para swap!, e ela é aplicada no valor atual do átomo lista-tarefas.

(defn excluir-tarefa [id]
  (swap! lista-tarefas (fn [tarefas] (remove #(= id (:id %)) tarefas))))
;; Na função "excluir-tarefa", aplicamos a função swap ao atom "lista-tarefas", mas precisamos passar outra função como parâmetro. Para não termos que criar outra função, usamos a função anônima, também conhecida em outras linguagens como lambda. Podemos observar o uso em clojure através da "fn" recebendo o parâmetro tarefas e outra função anônima, caracterizada com o "#" para captarmos o indicador da lista conforme o valor escolhido.
;; Também podemos perceber que a função remove também é outra função de alta ordem. Ela também aceita uma função como argumento. Aqui, a função anônima #(= id (:id %)) é passada como argumento para remove, indicando a condição de remoção dos elementos da sequência tarefas.

(defn listar-tarefas []
  (if (empty? @lista-tarefas)
    (println "Lista vazia")
    (do
      (println "Lista de tarefas:")
      (doseq [tarefa @lista-tarefas]
        (println (str (:id tarefa) ". " (:descricao tarefa))))
      (println))))

(defn menu-inicial []
  (loop []
    (println "Menu:")
    (println "1. Adicionar tarefa")
    (println "2. Listar tarefas")
    (println "3. Excluir tarefa")
    (println "4. Sair")
    (print "Escolha uma opcao: ")
    (flush)
    (let [opcao (read-line)]
      (cond
        (= opcao "1")
        (do
          (let [descricao (obter-descricao-tarefa)]
            (adicionar-tarefa descricao)
            (println "Tarefa adicionada.")
            (recur)))
        (= opcao "2")
        (do
          (listar-tarefas)
          (recur))
        (= opcao "3")
        (do
          (print "Digite o ID da tarefa que deseja excluir: ")
          (flush)
          (let [id (read-line)]
            (excluir-tarefa (Integer. id))
            (println "Tarefa excluida."))
          (recur))
        (= opcao "4")
        (do
          (println "Saindo...")
          :exit)
        :else
        (do
          (println "Opcao invalida. Tente novamente.")
          (recur))))))

(menu-inicial)
