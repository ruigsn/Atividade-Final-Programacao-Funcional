(ns atividade-final-tarefas.core)

(def lista-tarefas (atom []))

(defn obter-descricao-tarefa []
  (println "Digite a descricao da tarefa:")
  (read-line))

(defn adicionar-tarefa [descricao]
  (when (not (empty? descricao)) ; Verifica se a descrição não está vazia
    (let [id (if (empty? @lista-tarefas)
               1
               (->> @lista-tarefas (map :id) (apply max) inc))
          nova-tarefa {:id id :descricao descricao}]
      (swap! lista-tarefas conj nova-tarefa))))

(defn excluir-tarefa [id]
  (swap! lista-tarefas (fn [tarefas] (remove #(= id (:id %)) tarefas))))

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
