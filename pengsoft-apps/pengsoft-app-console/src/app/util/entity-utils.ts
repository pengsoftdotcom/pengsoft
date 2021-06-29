import { NzTreeNodeOptions } from 'ng-zorro-antd/tree';

export class EntityUtils {

    private static excludes = ['createdAt', 'updatedAt'];

    static toJSON(entity: any): string {
        if (entity) {
            this.excludes.forEach(name => {
                delete entity[name];
            });
            for (const name in entity) {
                if (entity.hasOwnProperty(name)) {
                    if (entity[name] === null || entity[name] === undefined || entity[name] === '') {
                        delete entity[name];
                    }
                }
            }
        }
        return encodeURIComponent(JSON.stringify(entity));
    }

    static fromJSON(json: string): any {
        if (json) {
            return JSON.parse(decodeURIComponent(json));
        } else {
            return null;
        }
    }

    static convertTreeEntityToTreeNode(entity: any): NzTreeNodeOptions {
        return {
            title: entity.name,
            value: entity,
            key: entity.id,
            isLeaf: entity.leaf,
            children: entity.children
        };
    }

    static convertTreeNodeToTreeEntity(node: NzTreeNodeOptions): any {
        const value = node.value;
        value.children = node.children;
        return value;
    }

    static convertTreeToList(tree: Array<NzTreeNodeOptions>, convert?: (node: NzTreeNodeOptions) => any): Array<any> {
        convert = convert ? convert : this.convertTreeNodeToTreeEntity;
        const list = [];
        const queue = [];
        if (tree) {
            tree.forEach(data => {
                queue.push(data);
                while (queue.length > 0) {
                    let parent = queue.pop();
                    parent = convert(parent);
                    list.push(parent);
                    if (parent.children) {
                        parent.children.reverse().forEach(child => queue.push(child));
                    }
                }
            });
        }
        return list;
    }

    static convertListToTree(list: Array<any>, convert?: (entity: any) => NzTreeNodeOptions): Array<NzTreeNodeOptions> {
        convert = convert ? convert : this.convertTreeEntityToTreeNode;
        const roots = [];
        const queue = [];
        let nodes = list.map(entity => convert(entity));
        nodes.filter(source => !source.value.parent || nodes.every(target => source.value.parent.id !== target.value.id))
            .forEach(node => {
                queue.push(node);
                roots.push(node);
            });
        nodes = nodes.filter(source => !roots.some(target => source.value.id === target.value.id));
        roots.sort(this.sort);
        while (queue.length > 0) {
            const node = queue.shift();
            const parentIds = node.value.parentIds ? node.value.parentIds + '::' + node.value.id : node.value.id;
            node.children = nodes.filter(child => child.value.parentIds === parentIds).sort(this.sort);
            nodes = nodes.filter(source => !node.children.some(target => source.value.id === target.value.id));
            node.children.forEach(child => queue.push(child));
        }
        return roots;
    }

    static sort(node1: NzTreeNodeOptions, node2: NzTreeNodeOptions): number {
        if (node1.value.code !== undefined && node2.value.code !== undefined) {
            if ([node1.value.code, node2.value.code].sort()[0] === node1.value.code) {
                return -1;
            } else {
                return 1;
            }
        }

        if (node1.value.sequence !== undefined && node2.value.sequence !== undefined) {
            if (node1.value.sequence > node2.value.sequence) {
                return 1;
            } else if (node1.value.sequence === node2.value.sequence) {
                return 0;
            } else {
                return -1;
            }
        }

        return 0;
    }

    static filterTree(tree: Array<NzTreeNodeOptions>, options: { filter: (node) => boolean }): Array<any> {
        if (tree) {
            tree = tree.filter(options.filter);
            const queue = [];
            tree.forEach(node => {
                queue.push(node);
                while (queue.length > 0) {
                    const parent = queue.shift();
                    if (parent.children) {
                        parent.children = parent.children.filter(options.filter);
                        parent.children.forEach(child => queue.push(child));
                    }
                }
            });
        }
        return tree;
    }

    static findTreeNodes(tree: Array<NzTreeNodeOptions>, ids: Array<string>): Array<NzTreeNodeOptions> {
        const queue = [];
        const matched = [];
        tree.forEach(node => queue.push(node));
        while (queue.length > 0) {
            const parent = queue.pop();
            if (ids.some(id => id === parent.key)) {
                matched.push(parent);
            }
            if (parent.children) {
                parent.children.forEach(child => queue.push(child));
            }
        }
        return matched;
    }

    static getEntityId(entity: any): string {
        if (typeof entity === 'object' && entity.hasOwnProperty('id')) {
            return entity.id;
        }
        if (typeof entity === 'string') {
            return entity;
        }
        return null;
    }

    static getEntityIds(entities: any): Array<string> {
        if (typeof entities === 'object' && entities.hasOwnProperty('id')) {
            return [entities.id];
        }
        if (typeof entities === 'string') {
            return [entities];
        }
        if (entities instanceof Array && entities.length > 0) {
            if (typeof entities[0] === 'object' && entities.hasOwnProperty('id')) {
                return entities.map(domain => domain.id);
            }
            if (typeof entities[0] === 'string') {
                return entities;
            }
        }
        return [];
    }

    static equals(entity1: any, entity2: any): boolean {
        if (!entity1 && !entity2) {
            return true;
        }

        if (entity1 === entity2) {
            return true;
        }

        if (entity1 && entity2 && entity1.id && entity2.id) {
            return entity1.id === entity2.id;
        }

        return false;
    }

}
