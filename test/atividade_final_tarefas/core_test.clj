(ns atividade-final-tarefas.core-test
  (:require [clojure.test :refer :all]
            [atividade-final-tarefas.core :refer :all]))

;; Função para inicializar o estado antes de cada teste
(defn inicializar-estado []
  (reset! lista-tarefas []))

;; Função para limpar o estado após cada teste
(defn limpar-estado []
  (reset! lista-tarefas []))

;; Teste para adição de tarefas

(deftest test-adicionar-tarefa-descricao-vazia
  (inicializar-estado)
  (testing "Adicionar uma tarefa com descricao vazia"
    (adicionar-tarefa "")
    (is (empty? @lista-tarefas))) ; A lista de tarefas deve continuar vazia
  (limpar-estado))

(deftest test-adicionar-tarefa-multiplas
  (inicializar-estado)
  (testing "Adicionar multiplas tarefas"
    (adicionar-tarefa "Tarefa 1")
    (adicionar-tarefa "Tarefa 2")
    (adicionar-tarefa "Tarefa 3")
    (is (= (count @lista-tarefas) 3)) ; Verifique se há três tarefas na lista
    (is (= (:descricao (first @lista-tarefas)) "Tarefa 1")) ; Verifique a primeira tarefa
    (is (= (:descricao (second @lista-tarefas)) "Tarefa 2")) ; Verifique a segunda tarefa
    (is (= (:descricao (last @lista-tarefas)) "Tarefa 3"))) ; Verifique a última tarefa
  (limpar-estado))

(deftest test-adicionar-tarefa-id
  (inicializar-estado)
  (testing "Adicionar uma tarefa e verificar o ID"
    (adicionar-tarefa "Minha tarefa")
    (let [tarefa (first @lista-tarefas)]
      (is (= (:descricao tarefa) "Minha tarefa")) ; Verifique a descrição
      (is (= (:id tarefa) 1))) ; Verifique o ID da tarefa
    (adicionar-tarefa "Outra tarefa")
    (let [tarefa (last @lista-tarefas)]
      (is (= (:descricao tarefa) "Outra tarefa")) ; Verifique a descrição
      (is (= (:id tarefa) 2)))) ; Verifique o ID da tarefa
  (limpar-estado))


(deftest test-adicionar-tarefa-descricao-vazia
  (inicializar-estado)
  (testing "Adicionar uma tarefa com descricao vazia"
    ;; Adicionar tarefa com descrição vazia
    (adicionar-tarefa "")
    ;; Verificar se a tarefa não foi adicionada (lista de tarefas deve estar vazia)
    (is (empty? @lista-tarefas))
    ;; Tentar adicionar outra tarefa com descrição não vazia
    (adicionar-tarefa "Tarefa não vazia")
    ;; Verificar se a tarefa foi adicionada corretamente
    (let [tarefa (first @lista-tarefas)]
      (is (not (empty? @lista-tarefas))) ; A lista de tarefas não deve estar vazia
      (is (= (:descricao tarefa) "Tarefa não vazia")))) ; Verifique a descrição da tarefa
  (limpar-estado))

;; Testes para a função excluir-tarefa

(deftest test-excluir-tarefa-inexistente
  (inicializar-estado)
  (testing "Tentar excluir uma tarefa inexistente"
    ;; Tente excluir uma tarefa com ID 1 (não deve existir)
    (excluir-tarefa 1)
    (is (empty? @lista-tarefas))) ; A lista de tarefas deve estar vazia
  (limpar-estado))

(deftest test-excluir-ultima-tarefa
  (inicializar-estado)
  (testing "Excluir a ultima tarefa em uma lista de varias tarefas"
    ;; Adicionar múltiplas tarefas
    (adicionar-tarefa "Tarefa 1")
    (adicionar-tarefa "Tarefa 2")
    (is (= (count @lista-tarefas) 2)) ; Verifique se há duas tarefas na lista
    ;; Excluir a última tarefa
    (excluir-tarefa 2)
    (is (= (count @lista-tarefas) 1)) ; Agora deve haver uma tarefa na lista
    (is (= (:descricao (first @lista-tarefas)) "Tarefa 1"))) ; Verifique a descrição
  (limpar-estado))

(deftest test-excluir-tarefa-existente
  (inicializar-estado)
  (testing "Excluir uma tarefa existente"
    ;; Adicionar duas tarefas
    (adicionar-tarefa "Tarefa 1")
    (adicionar-tarefa "Tarefa 2")
    ;; Excluir a primeira tarefa
    (excluir-tarefa 1)
    ;; Verificar se a primeira tarefa foi excluída
    (is (not (empty? @lista-tarefas))) ; A lista de tarefas não deve estar vazia
    (is (nil? (some #(= (:id %) 1) @lista-tarefas))) ; Verifique se a primeira tarefa foi excluída
    ;; Tentar excluir a segunda tarefa (que deve estar na lista)
    (excluir-tarefa 2)
    ;; Verificar se a segunda tarefa foi excluída
    (is (empty? @lista-tarefas))) ; A lista de tarefas deve estar vazia após a exclusão
  (limpar-estado))


;; Testes para a função listar-tarefas

(deftest test-listar-tarefas-vazia
  (inicializar-estado)
  (testing "Listar tarefas quando a lista esta vazia"
    (let [out (with-out-str (listar-tarefas))] ; Captura a saída da função
      (is (clojure.string/includes? out "Lista vazia")))) ; Verifique se a mensagem de lista vazia é exibida
  (limpar-estado))

(deftest test-listar-tarefas-uma-tarefa
  (inicializar-estado)
  (testing "Listar tarefas quando ha uma tarefa"
    ;; Adicionar uma tarefa
    (adicionar-tarefa "Tarefa de teste")
    (let [out (with-out-str (listar-tarefas))] ; Captura a saída da função
      (is (clojure.string/includes? out "1. Tarefa de teste")))) ; Verifique se a descrição da tarefa é exibida
  (limpar-estado))

(deftest test-listar-tarefas-multiplas
  (inicializar-estado)
  (testing "Listar tarefas quando ha varias tarefas"
    ;; Adicionar múltiplas tarefas
    (adicionar-tarefa "Tarefa 1")
    (adicionar-tarefa "Tarefa 2")
    (adicionar-tarefa "Tarefa 3")
    (let [out (with-out-str (listar-tarefas))] ; Captura a saída da função
      (is (clojure.string/includes? out "1. Tarefa 1")) ; Verifique a primeira tarefa
      (is (clojure.string/includes? out "2. Tarefa 2")) ; Verifique a segunda tarefa
      (is (clojure.string/includes? out "3. Tarefa 3")))) ; Verifique a terceira tarefa
  (limpar-estado))

;; Executar os testes
(run-tests)
