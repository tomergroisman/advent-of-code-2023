import networkx as nx

data = open("input", "r").read().rstrip().split("\n")

G = nx.Graph()

for line in data:
    node, neighbors = line.split(": ")
    for neighbor in neighbors.split(" "):
        G.add_edge(node, neighbor, capacity=1)

(e1, e2, e3) = nx.minimum_edge_cut(G)
G.remove_edge(e1[0], e1[1])
G.remove_edge(e2[0], e2[1])
G.remove_edge(e3[0], e3[1])

nodes = list(G)
q = [nodes[0]]
visited = set()
while q:
    node = q.pop()

    if node in visited:
        continue

    visited.add(node)

    for edge in G.edges:
        if edge[0] == node or edge[1] == node:
            q.append(edge[0])
            q.append(edge[1])

print(len(visited) * (len(nodes) - len(visited)))
